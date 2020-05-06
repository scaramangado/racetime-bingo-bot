package de.scaramangado.racetimebingobot.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.time.Instant

@Configuration
class JsonConfiguration {

  @Bean
  fun gson(): Gson = GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .registerTypeAdapter(Duration::class.java, durationDeserializer)
      .registerTypeAdapter(Instant::class.java, instantDeserializer)
      .registerTypeAdapterFactory(enumDeserializer)
      .create()

  private val durationDeserializer = JsonDeserializer { json, _, _ ->
    Duration.parse(json.asString)
  }

  private val instantDeserializer = JsonDeserializer { json, _, _ ->
    Instant.parse(json.asString)
  }

  private val enumDeserializer = object : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {

      if (type?.rawType?.isEnum != true) {
        return null
      }

      return object :  TypeAdapter<T>() {
        override fun write(writer: JsonWriter, value: T?) {
          value?.run { writer.value(value.toString().toLowerCase()) } ?: writer.nullValue()
        }

        override fun read(reader: JsonReader): T? {

          if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
          }

          val jsonValue = reader.nextString()

          @Suppress("UNCHECKED_CAST")
          return (type.rawType.enumConstants as Array<T>).findLast {
            it.toString().equals(jsonValue, ignoreCase = true)
          }
        }
      }
    }
  }
}
