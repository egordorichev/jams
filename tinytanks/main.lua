RELEASE = (arg[2] ~= "-debug")
DEBUG = not RELEASE
SCALE = 3
SCREEN_WIDTH = love.graphics.getWidth()
SCREEN_HEIGHT = love.graphics.getHeight()
WIDTH = love.graphics.getWidth() / SCALE
HEIGHT = love.graphics.getHeight() / SCALE

JOYSTICK = nil
MUSIC = 1
SOUNDS = 1

libs = require "l2.libs"

for i, m in ipairs(require "require") do
	local succes, message = pcall(require, m)

	if not succes then
		if message:match("not found:") then
			print(message)
			requireDir("l2/" .. m)
		else
			error(message)
		end
	end
end

function love.load()
	if DEBUG then
		game:init(WonState())
	 	-- game:init(InGameState())
		-- game:init(MenuState())
	else
		game:init(MenuState())
	end

	Input.register({
		[ "moveForward" ] = { "w", "up" },
		[ "moveBackward" ] = { "s", "down" },
		[ "rotateLeft" ] = { "a", "left" },
		[ "rotateRight" ] = { "d", "right" },
		[ "action1" ] = { "x", "v", "k" },
		[ "action2" ] = { "c", "l" }
	})

	UI.init()
	love.graphics.setDefaultFilter("nearest", "nearest")

	font = love.graphics.newImageFont("data/fonts/font.png", 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!?[](){}.,;:<>+=%#^*~/\\|$@&`"\'-_ ', 1)
	love.graphics.setFont(font)

	music = love.audio.newSource("data/music/main.mp3")
	music:setLooping(true)
	music:play()
end

function love.update(dt)
	libs.update(dt)
	game:update(dt)
end

function love.draw()
	game:draw()
	UI.reset()
end

function love.keypressed(key)
	game:keyPressed(key)
end

function love.mousepressed(x, y, button)
	UI.mouseDown()
	game:mousePressed(x, y, button)
end

function love.mousereleased(x, y, button, isTouch)
	UI.mouseUp()
end

function love.joystickadded(joystick)
    JOYSTICK = joystick
end

function love.quit()
	game:save()
end
