HealthKit = Entity:extend()

function HealthKit:new()
	HealthKit.super.new(self)
	self.angle = -30 + math.random(60)
	self:loadImage("data/images/healthKit.png")
	self.zIndex = 1
	self.solid = false
end

function HealthKit:onOverlap(e)
	if e:is(Tank) and not e.dead and not self.dead then
		e.health = e.health + 40
		if e.health > e.info.health then e.health = e.info.health end
		self.hurtText = FText(self, "+HEALTH")
		game.state.scene:add(self.hurtText)
		self:kill()

		if SOUNDS == 1 then
			Assets.load("data/sounds/pickup.wav"):play()
		end
	end
end