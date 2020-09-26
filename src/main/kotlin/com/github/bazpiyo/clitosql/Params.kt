package com.github.bazpiyo.clitosql

import java.io.File

data class Params(
    val inFile: InFile,
    val outFile: OutFile,
    val tableName: TableName
) {
    companion object {
        fun of(argList: List<String>): Params {
            if (argList.size != 6) {
                throw RuntimeException("引数が異なります")
            }
            return Params(
                inFile = InFile.of(argList),
                outFile = OutFile.of(argList),
                tableName = TableName.of(argList)
            )
        }
    }
}

private fun List<String>.getParam(flagStr: String): String {
    val fileFlagIndex = this.indexOf(flagStr)
    if (fileFlagIndex == -1) {
        throw RuntimeException("${flagStr}パラメータが見つかりません。")
    }
    return this.getOrNull(fileFlagIndex + 1)
        ?: throw RuntimeException("${flagStr}パラメータの値が見つかりません。")
}

data class InFile(val value: File) {
    fun read(): List<String> = value.readLines()

    companion object {
        const val flag = "-file"
        fun of(argList: List<String>): InFile {
            val fileStr = argList.getParam(flag)
            if (!File(fileStr).isFile) {
                throw RuntimeException("-fileの指定ファイルが存在しません")
            }
            return InFile(value = File(fileStr))
        }
    }
}

data class OutFile(val value: File) {
    companion object {
        const val flag = "-out"
        fun of(argList: List<String>): OutFile {
            val outStr = argList.getParam(flag)
            if (File(outStr).isFile) {
                throw RuntimeException("-outの指定ファイルが存在します")
            }
            return OutFile(value = File(outStr))
        }
    }
}

data class TableName(val value: String) {
    companion object {
        const val flag = "-table"
        fun of(argList: List<String>): TableName {
            val tableStr = argList.getParam(flag)
            return TableName(value = tableStr)
        }
    }
}