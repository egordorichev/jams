IsoMap = Entity:extend()

function IsoMap:new()
	IsoMap.super.new(self)
end

function IsoMap:init(t, w, h, tiles)
	self.tileSize = t
	self.tw = w
	self.th = h
	self.w = w * self.tileSize
	self.h = h * self.tileSize
	self:loadImage(tiles)

	self.tiles = {}

	local x
	local y

	for y = 1, self.th, 1 do
		for x = 1, self.tw, 1 do
			self:setTile(1, x, y)
		end
	end

	self.quads = {}

	for y = 1, self.image:getHeight() / self.th, 1 do
		for x = 1, self.image:getWidth() / self.tw, 1 do
			self.quads[x + (y - 1) * self.th] = love.graphics.newQuad(x * self.tw, y * self.th, self.tw, self.th, self.image:getWidth(), self.image:getHeight())
		end
	end
end

function IsoMap:getTile(x, y)
	return self.tiles[x + (y - 1) * self.th]
end

function IsoMap:setTile(t, x, y)
	self.tiles[x + (y - 1) * self.th] = t
end

function IsoMap:draw()
	love.graphics.draw(self.image, self.quads[1], 10, 10)
	love.graphics.draw(self.image, self.quads[1], 42, 10)
	love.graphics.draw(self.image, self.quads[1], 26, 18)
end

function IsoMap:update(dt)

end