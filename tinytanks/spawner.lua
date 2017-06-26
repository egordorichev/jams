Spawner = Entity:extend()

function Spawner:new()
	Spawner.super.new(self)

	self:loadImage("data/images/spawner.png", 8, 8)
	self:addAnimation("main", { 1, 2, 3 }, 10)
	self:playAnimation("main")
	self.solid = false
end

function Spawner:update(dt)
	Spawner.super.update(self, dt)

	if love.math.random(0, 600) == 0 and #self:getNearbyEntities(10, function(e)
			return e:is(HealthKit)
		end) == 0 then

		local h = HealthKit()

		h.x = self.x
		h.y = self.y

		game.state.scene:add(h)
	end
end