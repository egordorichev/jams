UI = {}
local button = {}
local mouse = false

function UI.init()
	button.texture = Assets.load("data/images/button.png")
	button.w = button.texture:getWidth()
	button.h = button.texture:getHeight()
	button.sw = button.w / 6
	button.sh = button.h / 2
	button.quads = {}

	for start = 0, 2 do
		button.quads[start] = {}

		for y = 0, 1 do
			for x = 0, 2 do
				button.quads[start][y * 2 + x] = love.graphics.newQuad(start * button.sw * 2 + x * button.sw,
					y * button.sh, button.sw, button.sh, button.w, button.h)
			end
		end
	end
end

function UI.mouseDown()
	mouse = true
end

function UI.mouseUp()
	mouse = false
end

function UI.reset()
	mouse = false
end

function UI.button(label, x, y, w, h)
	local down = false

	if Input.x > x and Input.x < x + w and Input.y > y and Input.y < y + h then
		if mouse == false then
			UI.area(1, x, y, w, h)
		else
			UI.area(2, x, y, w, h)
			down = true
			mouse = false

			if SOUNDS == 1 then
				Assets.load("data/sounds/click.wav"):play()
			end
		end
	else
		UI.area(0, x, y, w, h)
	end

	Util.drawTextWithStroke(label, x + (w - font:getWidth(label) - 3) / 2, y + (h - 7) / 2)

	return down
end

function UI.area(start, x, y, w, h)
	love.graphics.draw(button.texture, button.quads[start][0], x, y)
 	love.graphics.draw(button.texture, button.quads[start][1], x + button.sw, y, 0, (w - button.sw * 3) / button.sw, 1)
	love.graphics.draw(button.texture, button.quads[start][1], x + button.sw, y + h - button.sh, 0, (w - button.sw * 3) / button.sw, -1)
	love.graphics.draw(button.texture, button.quads[start][2], x, y + button.sh, 0, 1, (h - button.sh * 3) / button.sh)
	love.graphics.draw(button.texture, button.quads[start][3], x + button.sw, y + button.sh, 0, (w - button.sw * 3) / button.sw, (h - button.sh * 3) / button.sh)
	love.graphics.draw(button.texture, button.quads[start][0], x + w - button.sw, y, 0, -1, 1)
	love.graphics.draw(button.texture, button.quads[start][0], x, y + h - button.sh, 0, 1, -1)
	love.graphics.draw(button.texture, button.quads[start][0], x + w - button.sw, y + h - button.sh, 0, -1, -1)
	love.graphics.draw(button.texture, button.quads[start][2], x + w - button.sw, y + button.sh, 0, -1, (h - button.sh * 3) / button.sh)
end

function UI.healthBar(x, y, w, h, val, max, sec)
	if val > max then
		val = max
		sec = max
	elseif val < 0 then
		val = 0
		sec = 0
	end

	local r, g, b, a = love.graphics.getColor()
	love.graphics.setColor(0, 0, 0)
	love.graphics.rectangle("fill", x, y, w, h)
	love.graphics.setColor(29, 43, 83)
	love.graphics.rectangle("fill", x + 1, y + 1, w - 2, h - 2)
	love.graphics.setColor(255, 119, 168)
	love.graphics.rectangle("fill", x + 1, y + 1, Util.map(sec, 0, max, 0, w - 2), h - 2)
	love.graphics.setColor(255, 0, 77)
	love.graphics.rectangle("fill", x + 1, y + 1, Util.map(val, 0, max, 0, w - 2), h - 2)
	love.graphics.setColor(r, g, b, a)

	local label = lume.round(val) .. "/" .. max

	Util.drawTextWithStroke(label, x + (w - font:getWidth(label) - 3) / 2, y + (h - 4) / 2)
end

function UI.select(variants, current, x, y, w, h, l)
	local down = false

	if Input.x > x and Input.x < x + w and Input.y > y and Input.y < y + h then
		if mouse == false then
			UI.area(1, x, y, w, h)
		else
			UI.area(2, x, y, w, h)
			mouse = false
			down = true

			if SOUNDS == 1 then
				Assets.load("data/sounds/click.wav"):play()
			end

			current = current + 1

			if current > #variants then
				current = 1
			end
		end
	else
		UI.area(0, x, y, w, h)
	end

	local label = variants[current]

	if l then
		label = l .. " " .. label
	end

	Util.drawTextWithStroke(label, x + (w - font:getWidth(label) - 3) / 2, y + (h - 7) / 2)

	return down, current
end