resource "aws_dynamodb_table" "customer_counters_table" {
  name         = "counters"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "counter_id"
  range_key    = "timestamp"

  attribute {
    name = "counter_id"
    type = "S"
  }

  attribute {
    name = "timestamp"
    type = "N"
  }
}

resource "aws_dynamodb_table" "customer_villages_table" {
  name         = "villages"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }
}

# Village names population

resource "aws_dynamodb_table_item" "villaribo" {
  table_name = aws_dynamodb_table.customer_villages_table.name
  hash_key   = aws_dynamodb_table.customer_villages_table.hash_key

  item = <<ITEM
{
  "id": {"S": "1"},
  "village_name": {"S": "Villaribo"}
}
ITEM
}

resource "aws_dynamodb_table_item" "villabajo" {
  table_name = aws_dynamodb_table.customer_villages_table.name
  hash_key   = aws_dynamodb_table.customer_villages_table.hash_key

  item = <<ITEM
{
  "id": {"S": "10"},
  "village_name": {"S": "Villabajo"}
}
ITEM
}

resource "aws_dynamodb_table_item" "seyda_neen" {
  table_name = aws_dynamodb_table.customer_villages_table.name
  hash_key   = aws_dynamodb_table.customer_villages_table.hash_key

  item = <<ITEM
{
  "id": {"S": "20"},
  "village_name": {"S": "Seyda Neen"}
}
ITEM
}

resource "aws_dynamodb_table_item" "arroyo" {
  table_name = aws_dynamodb_table.customer_villages_table.name
  hash_key   = aws_dynamodb_table.customer_villages_table.hash_key

  item = <<ITEM
{
  "id": {"S": "30"},
  "village_name": {"S": "Arroyo"}
}
ITEM
}