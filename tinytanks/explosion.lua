Explosion = Entity:extend()

function Explosion:new(at, d)
	Explosion.super.new(self)

	self.x = at.x + (at.w - 22) / 2
	self.y = at.y + (at.h - 22) / 2
	self.solid = false
	self.d = d or true

	self:loadImage("data/images/explosion.png", 22, 22)
	self:addAnimation("main", { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, 20)
	self:playAnimation("main")
	self.timer = 11 * 0.05
	self.zIndex = 3

	self:playSound("data/sounds/explosion.wav")
end

function Explosion:update(dt)
	Explosion.super.update(self, dt)
	self.timer = self.timer - dt

	if self.timer <= 0 then
		self:kill()
	end
end

function Explosion:onOverlap(e)
	if e:is(Tank) and e.solid and not e.dead and self.d then
		e:hurt(30)
	end
end

Part = Entity:extend()

function Part:new(at, d)
	Part.super.new(self)

	self.x = at.x + (at.w - 8) / 2
	self.y = at.y + (at.h - 8) / 2
	self.solid = false
	self.d = d or true

	self:loadImage("data/images/part.png", 8, 8)
	self:addAnimation("main", { 1, 2 }, 10)
	self:playAnimation("main")
	self.timer = 2 * 0.1
	self.zIndex = 3
end

function Part:update(dt)
	Part.super.update(self, dt)
	self.timer = self.timer - dt

	if self.timer <= 0 then
		self:kill()
	end
end