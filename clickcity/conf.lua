function love.conf(t)
	t.releases = {
		title = "click city",
		package = "clickcity",
		loveVersion = "0.10.2",
		version = "0.1",
		author = "Egor Dorichev",
		email = "egordorichev@gmail.com",
		description = "Build your city",
		homepage = nil,
		identifier = "clickcity",
		excludeFileList = {},
		releaseDirectory = nil
	}

	t.identity = "clickcity"
	t.version = "0.10.2"

	t.window.title = "click city - @egordorichev"
	t.window.icon = "data/images/icon.png"
	t.window.width = 640
	t.window.height = 480
	t.window.resizable = false
	t.modules.joystick = true
end