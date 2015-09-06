package io.boxtape.cli.core.resolution

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Component
import java.net.URL

@Component
public open class FileDownloader {
    open fun downloadContent(url: URL): String {
        return IOUtils.toString(url)
    }

}
