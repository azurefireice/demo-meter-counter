provider "aws" {
  region  = "us-east-2"
}


/*
TODO: create bucket and DynamoDb table manually before creating TF config
resource "aws_s3_bucket" "bucket" {
  bucket = "demo-meter-counter-tf-backend"
}

resource "aws_dynamodb_table" "terraform_state_lock" {
  name           = "terraform-lock"
  read_capacity  = 5
  write_capacity = 5
  hash_key       = "LockID"
  attribute {
    name = "LockID"
    type = "S"
  }
}
*/

terraform {
  backend "s3" {
    bucket = "demo-meter-counter-tf-backend"
    key    = "terraform"
    region = "us-east-2"
    dynamodb_table = "terraform-lock"
  }
}