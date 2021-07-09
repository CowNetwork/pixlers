package network.cow.minigame.pixlers

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

/**
 * @author Benedikt Wüller
 */
class ImgurService(private val clientId: String, private val albumId: String) {

    private val client = HttpClient(CIO)

    suspend fun upload(image: BufferedImage) : String {
        val response: HttpResponse = this.client.submitForm {
            url("https://api.imgur.com/3/image")
            header(HttpHeaders.Authorization, "Client-ID $clientId")
            formData {
                append("album", albumId)
                append("image", image.toBase64())
                append("type", "base64")
            }
        }

        // TODO: return link to image
        return response.status.description
    }

    private fun BufferedImage.toBase64() : String {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(this, "PNG", outputStream)
        val base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray())
        outputStream.close()
        return base64
    }

}
