Util = {}

local white = { 255, 255, 255 }
local black = { 34, 32, 52 }

function Util.drawTextWithStroke(text, x, y, color, stroke)
	color = color or white
	stroke = stroke or black

	local r, g, b, a = love.graphics.getColor()

	love.graphics.setColor(stroke)

	for j = -1, 1 do
		for i = -1, 1 do
			love.graphics.print(text, x + i, y + j)
		end
	end

	love.graphics.setColor(color)
	love.graphics.print(text, x, y)

	love.graphics.setColor(r, g, b, a)
end

function Util.map(val, inMin, inMax, outMin, outMax)
	return (val - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
end

function Util.line(x1, y1, x2, y2, fn)
	local dx, dy = x2 - x1, y2 - y1
	local d = math.max(math.abs(dx), math.abs(dy))
	local x, y = x1, y1
	local res = false

	for i = 1, d do
		res = fn(math.floor(x), math.floor(y))

		if res then
			break
		end

		x = x + dx / d
		y = y + dy / d
	end

	return res
end