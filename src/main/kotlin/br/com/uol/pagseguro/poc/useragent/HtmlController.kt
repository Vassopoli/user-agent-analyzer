package br.com.uol.pagseguro.poc.useragent

import net.sf.uadetector.ReadableUserAgent
import net.sf.uadetector.service.UADetectorServiceFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import ua_parser.Client
import ua_parser.Parser

@Controller
class HtmlController {
    @GetMapping("/")
    fun page(model: Model, @RequestHeader("User-Agent") userAgent: String): String {
        model["userAgent"] = userAgent

        uadetectorResources(model, userAgent)
        uapJava(model, userAgent)
        comparision(model)
        return "page"
    }

    fun uadetectorResources(model: Model, userAgent: String) {
        val parser = UADetectorServiceFactory.getResourceModuleParser()
        val agent: ReadableUserAgent = parser.parse(userAgent)

        model["uadetectorResourcesBrowserType"] = agent.type.name
        model["uadetectorResourcesBrowserFamily"] = agent.name
        model["uadetectorResourcesBrowserVersion"] = agent.versionNumber.toVersionString()
        model["uadetectorResourcesOperatingSystemFamily"] = agent.operatingSystem.name
        model["uadetectorResourcesOperatingSystemVersion"] = (if (!agent.operatingSystem.versionNumber.toVersionString().isNullOrEmpty()) agent.operatingSystem.versionNumber.toVersionString() else "")!!
        model["uadetectorResourcesDevice"] = agent.deviceCategory.name
    }

    fun uapJava(model: Model, userAgent: String) {
        val uaParser = Parser()
        val c: Client = uaParser.parse(userAgent)

        model["uapJavaBrowserFamily"] = c.userAgent.family
        model["uapJavaBrowserVersion"] = "${c.userAgent.major}.${c.userAgent.minor}"
        model["uapJavaOperatingSystemFamily"] = c.os.family
        model["uapJavaOperatingSystemVersion"] = (if (!c.os.major.isNullOrEmpty()) listOfNotNull(c.os.major, c.os.minor).joinToString(".") else "")!!
        model["uapJavaDevice"] = c.device.family
    }

    fun comparision(model: Model) {
        model["isEqualsBrowserFamily"] = (model.getAttribute("uadetectorResourcesBrowserFamily") == model.getAttribute("uapJavaBrowserFamily"))!!
        model["isEqualsBrowserVersion"] = (model.getAttribute("uadetectorResourcesBrowserVersion") == model.getAttribute("uapJavaBrowserVersion"))!!
        model["isEqualsOperatingSystemFamily"] = (model.getAttribute("uadetectorResourcesOperatingSystemFamily") == model.getAttribute("uapJavaOperatingSystemFamily"))!!
        model["isEqualsOperatingSystemVersion"] = (model.getAttribute("uadetectorResourcesOperatingSystemVersion") == model.getAttribute("uapJavaOperatingSystemVersion"))!!
        model["isEqualsDevice"] = (model.getAttribute("uadetectorResourcesDevice") == model.getAttribute("uapJavaDevice"))!!
        model["isEqualsBrowserType"] = false
    }
}