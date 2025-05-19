rootProject.name = "catsApp"
include("controller")
include("service")
include("dao")
include("service:models")
findProject(":service:models")?.name = "models"
