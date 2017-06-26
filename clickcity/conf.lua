function love.conf(t)
	t.releases = {
		title = "tiny tanks",
		package = "tinytanks",
		loveVersion = "0.10.2",
		version = "0.1",
		author = "Egor Dorichev",
		email = "egordorichev@gmail.com",
		description = "Small game for #remake jam",
		homepage = nil,
		identifier = "tinytanks ",
		excludeFileList = {},
		releaseDirectory = nil
	}

	t.identity = "tinytanks"
	t.version = "0.10.2"

	t.window.title = "tiny tanks - @egordorichev"
	t.window.icon = "data/images/icon.png"
	t.window.width = 480
	t.window.height = 480
	t.window.resizable = false
	t.modules.joystick = true 
end