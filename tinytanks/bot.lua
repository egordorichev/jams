Bot = Tank:extend()
Bot.last = nil

function Bot:new(name)
	Bot.super.new(self, name)
	self.m = 1
	self.delay = 0
	self:randomDirection()
end

function Bot:reset()
	Bot.super.reset(self)
	self.health = self.info.health * HANDICAP
end

function Bot:hurt(...)
	Bot.super.hurt(self, ...)
	Bot.last = self
end

function Bot:update(dt)
	if self.dead then
		Bot.super.update(self, dt)
		return
	end

	self.delay = self.delay - dt

	if self.delay < 0 then
		self.delay = 0
	end

	if self:hasLineOfSight(player) then
		a = math.deg(self:getAngle(player)) + 90 - self.cannon.rotationAngle - self.angle

		if a > 0 then
			self.cannon:rotateRight()
		else
			self.cannon:rotateLeft()
		end

		self.cannon:shoot()
	else
		local a = 0 - self.cannon.rotationAngle

		if self.m < 0 then
			a = 180 - self.cannon.rotationAngle
		end

		if a > 0 then
			self.cannon:rotateRight()
		else
			self.cannon:rotateLeft()
		end

		if self.m < 0 then
			self:moveBackward()
		else
			self:moveForward()
		end
	end

	Bot.super.update(self, dt)
end

function Bot:randomDirection()
	self.m = love.math.random(-1, 1)
end

function Bot:onOverlap(e)
	Bot.super.onOverlap(self, e)

	if self.delay == 0 and e.solid and e:is(Tile) then
		if self.m < 0 then
			self.m = 1
		else
			self.m = -1
		end

		self.delay = 1
	end
end