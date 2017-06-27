function love.conf(t)
	t.releases = {
		title = "candyclicker",
		package = "candyclicker",
		loveVersion = "0.10.2",
		version = "0.1",
		author = "Egor Dorichev",
		email = "egordorichev@gmail.com",
		description = "CANDIES!",
		homepage = nil,
		identifier = "candyclicker",
		excludeFileList = {},
		releaseDirectory = nil
	}

	t.identity = "candyclicker"
	t.version = "0.10.2"

	t.window.title = "candie clicker - @egordorichev"
	t.window.icon = "data/images/icon.png"
	t.window.width = 640
	t.window.height = 480
	t.window.resizable = false
end