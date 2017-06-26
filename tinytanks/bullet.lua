Bullet = Entity:extend()

function Bullet:new(angle, speed, damage)
	Bullet.super.new(self)

	self.angle = angle

	speed = speed or 500
	local a = math.rad(angle - 90)

	self.moves = true
	self.velocity.x = math.cos(a) * speed
	self.velocity.y = math.sin(a) * speed

	self.origin.x = 4
	self.origin.y = 4
	self.done = false
	self.damage = lume.round(damage or 10 + lume.random(0, 5))

	self:loadImage("data/images/bullet.png")
end

function Bullet:onOverlap(e)
	if self.done then return end

	if e.solid and e:getDistance(self) < 8 then
		if e:is(Tank) then
			e:hurt(self.damage)
			e:flash()
			game:shake(0.8, 0.2)
			self:kill()
			self.done = true
			return
		elseif e:is(Tile) and not e.tree then
			game:shake(0.5, 0.2)
			self:kill()

			if e.breakable then
				game.state.scene:add(Explosion(self))
				map:setTile(12, e.tileX, e.tileY)
			else
				game.state.scene:add(Part(self))
			end
		end
	end
end