Input = {
	map = {},
	pressed = {},
	x = 0,
	y = 0
}

local function mouseButtonToKey(btn)
	return "mouse:" .. btn
end

function Input.update(dt)
	local scale = 1 / SCALE
	Input.x = love.mouse.getX() * scale
	Input.y = love.mouse.getY() * scale
end

function Input.register(id, keys)
	if type(id) == "table" then
    	for k, v in pairs(id) do
			Input.register(k, v)
    	end

		return
	end

	Input.map[id] = lume.clone(keys)
end

function Input.onKeyPress(k)
	Input.pressed[k] = true
end

function Input.onMousePress(x, y, btn)
	Input.pressed[mouseButtonToKey(btn)] = true
end

function Input.reset(dt)
	lume.clear(Input.pressed)
end

function Input.isDown(id)
	local t = Input.map[id]
	assert(t, "Bad id")

	for i, v in ipairs(t) do
    	local x, d = v:match("(mouse):(%d+)")

    	if x and love.mouse.isDown(d) then
    		return true
    	end
	end

	return love.keyboard.isDown(unpack(t))
end

function Input.wasPressed(id)
	assert(Input.map[id], "Bad id")

	return lume.any(Input.map[id], function(k)
		return Input.pressed[k]
	end)
end

function Input.getMousePosition()
	return Input.x, Input.y
end