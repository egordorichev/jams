Player = Tank:extend()

function Player:new(name)
	Player.super.new(self, name)

	self.id = "player: " .. self.name
	self.x = 64
	self.y = 64
	self.lives = 3
end

player = Player("T-18")