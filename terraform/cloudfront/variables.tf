variable "frontend_bucket" {
  description = "The S3 bucket object for the frontend"
  type        = object({
    id                          = string
    arn                         = string
    bucket                      = string
    bucket_domain_name          = string
    bucket_regional_domain_name = string
  })
}