Point = Object:extend()

function Point:new(x, y)
    self.x = x or 0
    self.y = y and y or self.x
end

function Point:set(x, y)
	self.x = x or self.x
	self.y = y or self.y
end

function Point:add(p)
	self.x = self.x + p.x
	self.y = self.y + p.y
end

function Point:rotate(phi)
	phi = math.rad(phi)

	local c = math.cos(phi)
	local s = math.sin(phi)

	self.x = c * self.x - s * self.y
	self.y = s * self.x + c * self.y

	return self
end

function Point:clone(p)
	dest = dest or Point()

    dest.x = self.x
    dest.y = self.y

    return dest
end

function Point:overlaps(r)
	local x1, y1, x2, y2 = self.x, self.y, r.x, r.y

	return  x1 > x2 and
		x1 < x2 + r.width and
		y1 > y2 and
		y1 < y2 + r.height
end

function Point:get()
	return self.x, self.y
end

function Point:distance(p)
	return lume.distance(self.x, self.y, p.x, p.y)
end

function Point:_str()
	return  "x: " .. math.floor(self.x * 100) / 100 .. ", y: " .. math.floor(self.y * 100) / 100
end

function Point:__tostring()
	return lume.tostring(self, "Point")
end