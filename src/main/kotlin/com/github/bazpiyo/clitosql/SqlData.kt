package com.github.bazpiyo.clitosql

data class SqlData(
    val sqlLine: List<SqlLine>
) {

    fun toText(): String =
        sqlLine.joinToString("\n") {
            it.value
        }

    fun write(outFile: OutFile) {
        outFile.value.writeText(toText())
        println("書き込みを行いました")
    }

    companion object {
        fun of(tableName: TableName, csvData: CsvData): SqlData {
            val columnStr = toColumn(csvData.csvHeader)
            val valuesStr = toValues(csvData.csvBody)
            return valuesStr.map {
                SqlLine.of(
                    tableName = tableName.value,
                    columns = columnStr,
                    values = it
                )
            }.let { SqlData(it) }
        }


        fun toColumn(csvHeader: CsvHeader): String =
            csvHeader.csvHeaderName
                .joinToString(",") {
                    it.value
                }

        fun toValues(csvBody: CsvBody): List<String> =
            csvBody.csvRecords.map { rec ->
                rec.csvValue.joinToString(",") { value ->
                    value.toSqlStr()
                }
            }
    }
}

data class SqlLine(val value: String) {
    companion object {
        val template = """
            insert into ?tableName?(?columns?) values(?value?);
        """.trimIndent()

        fun of(tableName: String, columns: String, values: String): SqlLine =
            template
                .replace("?tableName?", tableName)
                .replace("?columns?", columns)
                .replace("?value?", values)
                .let { SqlLine(it) }
    }
}