Tile = Entity:extend()

function Tile:new(image, size, index)
	Tile.super.new(self)

	self:loadImage(image, size, size)

	self.frame = index
	self.last = self
	self.moves = false
	self.static = true
end

function Tile:setSlope(left, right, side)
	side = side or "top"

	assert(side == "top" or side == "bottom", "expected 'top' or 'bottom' as side")

	self.solid = false
	self.slope = { left = left, right = right, side = side }
end

function Tile:onOverlap(e)
	if not e.solid then
		return
	end

	local s = self.slope

	if s then
		local h = self.h
		local x = (s.left > s.right) and e:left() or e:right()
		local p = (x - self.x) / self.e

		if s.side == "bottom" then
			local y = self.y + lume.lerp(s.left, s.right, p) * h

			if e:top() < y and e.last:top() >= self:top() then
				e:top(y)

				if e.velocity.y < 0 then
					e.velocity.y = e.velocity.y * -e.bounce
				end

				e.touching.top = true
			end
		elseif s.side == "top" then
			local y = self.y + (1 - lume.lerp(s.left, s.right, p)) * h

			if e:bottom() > y and e.last:bottom() <= self:bottom() then
				e:bottom(y)

				if e.velocity.y > 0 then
					e.velocity.y = e.velocity.y * -e.bounce
				end

				e.touching.bottom = true
			end
		end
	end
end

Tilemap = Entity:extend()
Tilemap.Tile = Tile

function Tilemap:new()
	Tilemap.super.new(self)

	self.solid = false
	self.static = true
	self.zIndex = -1
end

function Tilemap:loadMetaImage(filename, map)
	local meta = love.image.newImageData(filename)
	assert(self.tiles, "expected tilemap to be loaded")

	if self.tiles[1].image:getWidth() ~= meta:getWidth() or self.tiles[1].image:getHeight() ~= meta:getHeight()	then
		error("tile image and meta image dimensions mismatch")
	end

	for i, t in ipairs(self.tiles) do
		local ts = self.tileSize
		local xtiles = meta:getWidth() / ts
		local px = ts * ((i - 1) % xtiles)
		local py = ts * math.floor((i - 1) / xtiles)
		local clr = string.format("#%02x%02x%02x", meta:getPixel(px, py))

		lume.call( map[clr], t, i )
	end
end

function Tilemap:loadArray(array, width, imageFile, tileSize)
	if #array % width ~= 0 then
		error("expected array to be divisible by width")
	end

	self.data = lume.clone(array)
	self.widthInTiles = width
	self.heightInTiles = #array / width
	self.tileSize = tileSize
	self.tiles = {}

	for i = 1, math.huge do
		local tile = Tile(imageFile, tileSize, i)
		table.insert(self.tiles, tile)

		if i == #tile.frames then
			break
		end
	end

	self.w = self.widthInTiles * self.tileSize
	self.h = self.heightInTiles * self.tileSize
end

function Tilemap:loadLua(filename, objHandler, tileLayer)
	local t = love.filesystem.load(filename)()

	self.tileset = t.tilesets[1]

	local image = self.tileset.image:gsub("%.%.", "data")
	local tilemap = t.layers[1]

	self.tileSize = self.tileset.tilewidth
	self.data = {}

	if tileLayer then
		tilemap = lume.match(t.tilemaps, function(x)
			return x.name == tileLayer
		end)
	end

	self:loadArray(tilemap.data, t.width, image, self.tileSize)

	if objHandler then
		for i, l in ipairs(t.layers) do
			if l.type == "objectgroup" then
				for i, o in ipairs(l.objects) do
					for k, v in pairs(o) do
						o[k] = tonumber(v) or v
					end

					objHandler(l.name, o)
				end
			end
		end
	end

	return self
end

function Tilemap:getTile(x, y)
	local t = self.tiles[self.data[x + y * self.widthInTiles + 1]]

	if t then
		t.x, t.y = x * self.tileSize + self.x, y * self.tileSize + self.y
		t.tileX, t.tileY = x, y

		return t
	end
end

function Tilemap:setTile(t, x, y)
	self.data[x + y * self.widthInTiles + 1] = t
end

local overlapsState
local overlapsSolid = function(t, e)
	if t.solid and e:overlaps(t) then
		overlapsState = true
	end
end

function Tilemap:overlapsSolid(e)
	overlapsState = false
	self:eachOverlappingTile(e, overlapsSolid, nil, nil, e)
	return overlapsState
end

function Tilemap:eachOverlappingTile(r, fn, revx, revy, ...)
	local sx = math.floor((r:left()	- self.x) / self.tileSize)
	local sy = math.floor((r:top() - self.y) / self.tileSize)
	local ex = math.floor((r:right() - self.x) / self.tileSize)
	local ey = math.floor((r:bottom() - self.y) / self.tileSize)
	sx, sy = math.max(sx, 0), math.max(sy, 0)
	ex = math.min(ex, self.widthInTiles - 1)
	ey = math.min(ey, self.heightInTiles - 1)

	if revx then
		sx, ex = ex, sx
	end

	if revy then
		sy, ey = ey, sy
	end

	for y = sy, ey, (revy and -1 or 1) do
		for x = sx, ex, (revx and -1 or 1) do
			local t = self:getTile(x, y)

			if t then
				fn(t, ...)
			end
		end
	end
end

local onOverlap = function(t, e)
	e:onOverlap(t)
	t:onOverlap(e)
end

function Tilemap:onOverlap(e)
	self:eachOverlappingTile(e, onOverlap, e.velocity.x < 0, e.velocity.y < 0, e)
end

function Tilemap:update(dt)
	Tilemap.super.update(self, dt)
	lume.each(self.tiles, "update", dt)
end

function Tilemap:draw()
	self:eachOverlappingTile(game.state.scene.camera, Entity.draw)
end

map = Tilemap()