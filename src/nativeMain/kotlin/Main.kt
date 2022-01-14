import libcurl.*
import kotlinx.cinterop.*

fun main() {
    println("Hello 4, Kotlin/Native!")
    val curl = curl_easy_init()  ?: throw RuntimeException("Could not initialize an easy handle")


        curl_easy_setopt(curl, CURLOPT_URL, "http://jonnyzzz.com")
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L)
        val res = curl_easy_perform(curl)
        if (res != CURLE_OK) {
            println("curl_easy_perform() failed ${curl_easy_strerror(res)?.toKString()}")
        } else {
            println("curl_easy_perform() ok!")
        }
        curl_easy_cleanup(curl)

}