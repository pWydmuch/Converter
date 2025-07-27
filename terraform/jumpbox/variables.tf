variable "vpc_settings" {
  type = object({
    security_group_id = string
    subnet_id         = string
    vpc_id            = string
  })
}