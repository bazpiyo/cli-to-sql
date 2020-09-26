package com.github.bazpiyo.clitosql

data class CsvData(
    val csvHeader: CsvHeader,
    val csvBody: CsvBody
) {
    companion object {
        const val sprintStr = ","
        fun readCsvData(inFile: InFile): CsvData {
            val lines = inFile
                .read()
                .map { it.split(sprintStr) }
            if (lines.isEmpty()) {
                throw RuntimeException("csvのデータが存在しません。")
            }
            if (lines.size == 1) {
                throw RuntimeException("csvのヘッダーとボディーを指定してください")
            }
            if (lines.map(List<String>::size).distinct().size != 1) {
                throw RuntimeException("カラム個数が異なります")
            }
            val csvHeader = CsvHeader.of(lines.first())
            val csvBody = CsvBody.of(lines.drop(1))
            return CsvData(csvHeader, csvBody)
        }
    }
}

data class CsvHeader(val csvHeaderName: List<CsvHeaderName>) {
    companion object {
        fun of(lines: List<String>): CsvHeader =
            lines
                .map { CsvHeaderName(it) }
                .let { CsvHeader(it) }
    }
}

data class CsvHeaderName(val value: String)

data class CsvBody(val csvRecords: List<CsvRecords>) {
    companion object {
        fun of(lines: List<List<String>>): CsvBody =
            lines.map { line ->
                line
                    .map { CsvValue(it) }
                    .let { CsvRecords(it) }
            }.let { CsvBody(it) }
    }
}

data class CsvRecords(val csvValue: List<CsvValue>)
data class CsvValue(val value: String) {
    fun toSqlStr(): String =
        when {
            value.toIntOrNull() != null -> value
            else -> """'$value'"""
        }
}