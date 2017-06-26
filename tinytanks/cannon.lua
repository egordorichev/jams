Cannon = Entity:extend()
Cannon.shotFrame = Assets.load("data/images/shot.png")

function Cannon:new(tank)
	Cannon.super.new(self)

	self.tank = tank

	self:loadImage("data/images/" .. self.tank.name .. ".png", 16, 20)
	self:addAnimation("main", { 2 }, 20)
	self:playAnimation("main")

	self.rotationAngle = 0
	self.speed = 1
	self.moves = true
	self.origin.x = 8
	self.origin.y = 12
	self.delay = 0
	self.justShot = 0
	self.reloadTime = self.tank.info.reloadTime
end

function Cannon:update(dt)
	Cannon.super.update(self, dt)

	self.delay = self.delay - dt

	if self.delay < 0 then
		self.delay = 0
	end

	self.x = self.tank.x
	self.y = self.tank.y
	self.angle = self.tank.angle + self.rotationAngle
	self.angularVelocity = self.angularVelocity * 0.5
end

function Cannon:draw()
	Cannon.super.draw(self)

	if self.justShot > 0 then
		local a = math.rad(self.angle - 90)
		local x = self.x + math.cos(a) * 12 + self.origin.x
		local y = self.y + math.sin(a) * 12 + self.origin.y

		love.graphics.draw(Cannon.shotFrame, x, y, 0, 1, 1, 4, 4)
	end

	self.justShot = self.justShot - 1
end

function Cannon:rotateRight()
	self.rotationAngle = self.rotationAngle + self.speed
end

function Cannon:rotateLeft()
	self.rotationAngle = self.rotationAngle - self.speed
end

function Cannon:shoot()
	if self.delay ~= 0 then
		return
	end

	self.delay = self.reloadTime
	self.justShot = 3

	local bullet = Bullet(self.angle, 500, self.tank.info.damage or 10)
	local a = math.rad(self.angle - 90)

	bullet.x = self.x + math.cos(a) * 15 + self.origin.x / 2
	bullet.y = self.y + math.sin(a) * 15 + self.origin.y / 2
	local a = math.rad(self.angle - 90)

	game:shake(1, 0.2)
	game.state.scene:add(bullet)

	self:playSound("data/sounds/shot.wav")
end