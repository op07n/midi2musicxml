package com.pokosho.midi2musicxml.cui

import com.pokosho.midi2musicxml.MidiParser
import com.pokosho.midi2musicxml.executor.NeutrinoExecutor

/**
 * Command Line Interface.
 * See help()
 */
class Cui(val args: Array<String>) {
  fun run(): Int {
    val params: Params?
    try {
      params = Params(args, System.`in`)
    } catch (e: IllegalArgumentException) {
      System.err.println(e.message)
      println(Params.help())
      return -1
    }

    if (params.showHelp) {
      println(Params.help())
      return 0
    }

    val parser = MidiParser()
    parser.parse(params.midiFile, params.lyric())

    if (!params.silent) {
      (params.warnings + parser.warnings).forEach { System.err.println(it) }
    }

    parser.generateXML(params.outputPath)
    println("Completed: ${params.outputPath}")
    if (params.neutrinoDir.isNotBlank()) {
      NeutrinoExecutor(params.neutrinoDir, params.outputPath).execute()
    }
    return 0
  }
}