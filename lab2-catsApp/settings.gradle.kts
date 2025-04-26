rootProject.name = "lab2-catsApp"
include("controller")
include("service")
include("dao")
include("service:models")
findProject(":service:models")?.name = "models"
