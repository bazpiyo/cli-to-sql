package com.github.bazpiyo.clitosql

import kotlin.system.exitProcess

// -file ファイル名 -table テーブル名 -out 出力先のファイル名
fun main(args: Array<String>) {
    println("処理開始")
    val argList = args.toList()
    runCatching {
        val params = Params.of(argList)
        val csvData = CsvData.readCsvData(params.inFile)
        val sqlData = SqlData.of(params.tableName, csvData)
        sqlData.write(params.outFile)
    }.onSuccess {
        println("処理成功")
    }.onFailure {
        println("処理失敗")
        it.printStackTrace()
        exitProcess(1)
    }
}