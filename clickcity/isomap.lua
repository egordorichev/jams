IsoMap = Entity:extend()

function IsoMap:new()
	IsoMap.super.new(self)
end

function IsoMap:init(t, w, h, tiles)
	self.t = t
	self.tw = w
	self.th = h
	self.w = w * self.t
	self.h = h * self.t
	self:loadImage(tiles)

	self.tiles = {}

	local x
	local y

	for y = 1, self.th, 1 do
		for x = 1, self.tw, 1 do
			self:setTile(9, x, y)
		end
	end

	self.quads = {}
	self.tr = self.image:getWidth() / self.t

		for x = 1, self.image:getWidth() / self.t, 1 do
			for y = 1, self.image:getHeight() / self.t, 1 do
			self.quads[x + (y - 1) * self.tr] = love.graphics.newQuad((x - 1) * self.t, (y - 1) * self.t, self.t, self.t, self.image:getWidth(), self.image:getHeight())
		end
	end

	-- DEBUG!

	self:setTile(1, 1, 1)
	self:setTile(1, 2, 1)
	self:setTile(1, 1, 2)
end

function IsoMap:load(file)
	-- todo
end

function IsoMap:save(file)

end

function IsoMap:getTile(x, y)
	return self.tiles[x + (y - 1) * self.th]
end

function IsoMap:setTile(t, x, y)
	self.tiles[x + (y - 1) * self.th] = t
end

function IsoMap:draw()
	local x
	local y

	for y = 1, self.th, 1 do
		for x = 1, self.tw, 1 do
			local t = self:getTile(x, y)

			if y % 2 == 1 then
				love.graphics.draw(self.image, self.quads[t],
					(x - 1) * self.t, (y - 1) * self.t - (y) * self.t / 4 * 3 + 24) -- FIXME! magic numbers!
			else
				love.graphics.draw(self.image, self.quads[t],
					(x - 1) * self.t + self.t / 2, (y - 1) * self.t - self.t / 4 * 3 - (y) * self.t / 4 * 3 + 48)
			end
		end
	end
end

function IsoMap:update(dt)

end