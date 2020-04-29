package com.pokosho.midi2musicxml.gui

import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.DirectoryChooser
import tornadofx.*
import java.io.File


class MainView : View("Midi2MusicXML") {
  override val root: AnchorPane by fxml("/fxml/main_view.fxml")
  private val buttonLazySearch: Button by fxid("buttonLazySearch")
  private val buttonSelectNeutrino: Button by fxid("buttonSelectNeutrino")
  private val buttonSelectMidi: Button by fxid("buttonSelectMidi")
  private val buttonSelectLyric: Button by fxid("buttonSelectLyric")
  private val buttonPreview: Button by fxid("buttonPreview")
  private val buttonGenerate: Button by fxid("buttonGenerate")
  private val textPathToNeutrino: TextField by fxid("textPathToNeutrino")
  private val textPathToInputMid: TextField by fxid("textPathToInputMid")
  private val textPathToLyric: TextField by fxid("textPathToLyric")
  private val textPreview: TextArea by fxid("textPreview")
  private val textMessage: TextArea by fxid("textMessage")

  init {
    buttonLazySearch.action {
      val dir = lazySearchNeutrino()
      if (dir == null) {
        textMessage.text = "NEUTRINOが見つかりませんでした"
      }
      textPathToNeutrino.text = dir?.absolutePath
    }

    buttonSelectNeutrino.action {
      val directoryChooser = DirectoryChooser()
      directoryChooser.title = "ディレクトリ選択"
      val path = directoryChooser.showDialog(this.currentWindow)
      textPathToNeutrino.text = path.absolutePath
    }
  }

  private fun lazySearchNeutrino(): File? {
    val env = System.getenv()
    var targets = listOf("/Applications", "~/Desktop", "~/Documents", "~/")
    if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
      targets = listOf(env["HOMEPATH"] ?: "" , env["ProgramFiles(x86)"] ?: "", env["ProgramFiles"] ?: "").filter { it.isNotBlank() }
    }
    targets.forEach {
      val dir = File(it)
      val founds = dir.listFiles().filter { it.name == "NEUTRINO" }
      if (founds.size > 0 && founds.first().isDirectory) {
        if (File(founds.first().absolutePath + "/bin/NEUTRINO").exists()) {
          return founds.first()
        }
      }
    }
    return null
  }
}

