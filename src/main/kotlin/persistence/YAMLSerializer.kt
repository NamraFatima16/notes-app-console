package persistence
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import models.Note
import java.io.File
import java.lang.Exception

class YAMLSerializer(private val file: File) : Serializer {
    @Throws(Exception::class)
    override fun read(): Any {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule().findAndRegisterModules()
        return mapper.readValue<List<Note>>(file)
    }

    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule().findAndRegisterModules()
        mapper.writeValue(file, obj)
    }
}
