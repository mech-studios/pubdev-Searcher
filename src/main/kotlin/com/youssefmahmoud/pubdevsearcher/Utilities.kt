package com.youssefmahmoud.pubdevsearcher

import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.ShellTerminalWidget
import org.jetbrains.plugins.terminal.TerminalToolWindowManager

class Utilities {
    companion object {
        private var terminalWidget: ShellTerminalWidget? = null
        fun runCommandInJetBrainsTerminal(project: Project, command: List<String>) {
            val terminalToolWindowManager = TerminalToolWindowManager.getInstance(project)
            val closed = terminalWidget?.isSessionRunning
           if (closed == null || !closed) {
               //check if there is already a terminal session running with the name "PubDevRunner" by iterating over all the terminal sessions
                terminalToolWindowManager.terminalWidgets.forEach {
                    if (it.terminalTitle.defaultTitle == "PubDevRunner") {
                        //Remove it and create a new one
                        terminalToolWindowManager.terminalWidgets.remove(it)
                        terminalWidget = terminalToolWindowManager.createLocalShellWidget(
                            project.basePath,
                            "PubDevRunner"
                        )
                    }
                }
               if (terminalWidget == null) {
                   terminalWidget = terminalToolWindowManager.createLocalShellWidget(
                       project.basePath,
                       "PubDevRunner"
                   )
               }
            }

            for (cmd in command) {
                terminalWidget?.executeCommand(cmd)
            }
        }
    }
}