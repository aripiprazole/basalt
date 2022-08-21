package basalt.extension

import andesite.event.MinecraftEvent

data class ExtensionLoadEvent(val extension: Extension) : MinecraftEvent

data class ExtensionEnableEvent(val extension: Extension) : MinecraftEvent

data class ExtensionDisableEvent(val extension: Extension) : MinecraftEvent
