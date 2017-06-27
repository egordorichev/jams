Camera = Entity:extend()

function Camera:new(follow)
    Camera.super.new(self)
    self.follow = follow
    self.speed = 0.2
	self.w = WIDTH
	self.h = HEIGHT
end

function Camera:set()
    love.graphics.push()
    love.graphics.translate(-self.x, -self.y)
end

function Camera:unset()
    love.graphics.pop()
end

function Camera:update(dt)
    if self.follow ~= nil then
		local a = math.rad(self.follow.cannon.angle - 90)

		if self.follow.dead then
        	self.x = lume.clamp(lume.smooth(self.x, self.follow:centerX() - self.w / 2, self.speed), 0, map.w - WIDTH)
        	self.y = lume.clamp(lume.smooth(self.y, self.follow:centerY() - self.h / 2, self.speed), 0, map.h - HEIGHT)
		else
			self.x = lume.clamp(lume.smooth(self.x, self.follow:centerX() - self.w / 2 + math.cos(a) * 40, self.speed), 0, map.w - WIDTH)
			self.y = lume.clamp(lume.smooth(self.y, self.follow:centerY() - self.h / 2 + math.sin(a) * 40, self.speed), 0, map.h - HEIGHT)
		end
	end
end