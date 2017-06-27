InGameState = State:extend()

function InGameState:init()
	InGameState.super.init(self)
	self.name = "in game"
end

function InGameState:update(dt)
	InGameState.super.update(self, dt)
end

function InGameState:draw()
	InGameState.super.draw(self)
end

function InGameState:drawUI()
	UI.button("test", 8, 8, 48, 24)
end