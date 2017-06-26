Tank = Entity:extend()

function Tank:new(name)
	Tank.super.new(self)

	self.id = "tank"
	self.name = name

	local tanks = require "data.tanks"

	self.info = tanks[self.name] or tanks["T-18"]

	self:loadImage("data/images/" .. self.name .. ".png", 16, 20)
	self:addAnimation("stop", { 1 }, 1)
	self:addAnimation("forward", { 1, 3 }, 8)
	self:addAnimation("back", { 3, 1 }, 8)
	self:addAnimation("dead", { 4 }, 1)
	self:playAnimation("stop")

	self.speed = self.info.speed
	self.moves = true
	self.solid = true
	self.cannon = Cannon(self)
	self.origin.x = 8
	self.origin.y = 12
	self.move = 0
	self.hurtText = nil
	self.removable = false

	local r = love.math.random(0, 3)

	if r == 0 then
		self.angle = 0
	elseif r == 1 then
		self.angle = 90
	elseif r == 2 then
		self.angle = 180
	elseif r == 3 then
		self.angle = 270
	end

	self:reset()
end

function Tank:reset()
	self:playAnimation("stop")
	self.dead = false
	self.health = self.info.health
	self.solid = true
	self.zIndex = 2
	self:hurt(0)
end

function Tank:hasLineOfSight(e)
	local x1 = math.floor(self:centerX() / map.tileSize)
	local y1 = math.floor(self:centerY() / map.tileSize)
	local x2 = math.floor(e:centerX() / map.tileSize)
	local y2 = math.floor(e:centerY() / map.tileSize)

	local res = Util.line(x1, y1, x2, y2, function(x, y)
		local t = map:getTile(x, y)
		return t and t.solid
	end)

	return not res
end

function Tank:flicker()
	Tank.super.flicker(self)
	self.cannon:flicker()
end

function Tank:flash()
	Tank.super.flash(self)
	self.cannon:flash()
end

function Tank:update(dt)
	Tank.super.update(self, dt)

	if self.dead then
		return
	end

	self.cannon:update(dt)
	self.accel.y = 0
	self.accel.x = 0
	self.move = 0
	self.velocity.x = self.velocity.x * 0.9
	self.velocity.y = self.velocity.y * 0.9
	self.angularVelocity = self.angularVelocity * 0.5
end

function Tank:updateMovement(dt)
	if dt == 0 or self.dead then
		return
	end

	self.last = Rect.clone(self, self.last)

	local a = math.rad(self.angle - 90)

	self.x = self.x + math.cos(a) * self.speed * self.move
	self.y = self.y + math.sin(a) * self.speed * self.move

	self.angle = self.angle + self.angularVelocity * dt

	if self.move == 0 then
		self:playAnimation("stop")
	end
end

function Tank:draw()
	Tank.super.draw(self)
	self.cannon:draw()
end

function Tank:moveForward()
	self.move = 1
	self:playAnimation("forward")
end

function Tank:moveBackward()
	self.move = -1
	self:playAnimation("back")
end

function Tank:rotateLeft()
	self.angularVelocity = self.angularVelocity - self.speed * 40
end

function Tank:rotateRight()
	self.angularVelocity = self.angularVelocity + self.speed * 40
end

function Tank:onOverlap(e)
	if e.solid and e:getDistance(self) < 32 and self.solid then
		self:separate(e)
	end
end

function Tank:hurt(amount)
	Tank.super.hurt(self, amount)

	if amount == 0 then
		return
	end

	if self.hurtText and not self.hurtText.dead and self:getDistance(self.hurtText) < 40 then
	 	local t = math.abs(-tonumber(self.hurtText.text) + amount) * -1
	 	self.hurtText:reset(tostring(t))
	else
		self.hurtText = FText(self, tostring(-amount))
		game.state.scene:add(self.hurtText)
	end
end

function Tank:kill()
	Tank.super.kill(self)
	self:playAnimation("dead")
	self.solid = false
	self.zIndex = 1

	local e = Explosion(self)
	game.state.scene:add(e)
end