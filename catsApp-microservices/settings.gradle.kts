rootProject.name = "catsApp-microservices"
include("web-gateway")
include("cat-service")
include("owner-service")
include("shared")
include("cat-service:cats-client")
findProject(":cat-service:cats-client")?.name = "cats-client"
include("cat-service:cats-core")
findProject(":cat-service:cats-core")?.name = "cats-core"
include("owner-service:owners-client")
findProject(":owner-service:owners-client")?.name = "owners-client"
include("owner-service:owners-core")
findProject(":owner-service:owners-core")?.name = "owners-core"
