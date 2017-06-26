GameOverState = State:extend()

function GameOverState:init()
	GameOverState.super.init(self)
	self.name = "game over"
end

function GameOverState:update(dt)
	GameOverState.super.update(self, dt)
end

function GameOverState:draw()
	GameOverState.super.draw(self)

	Util.drawTextWithStroke("Game over!", 15, 15)

	if UI.button("To menu", 10, HEIGHT - 30, 64, 22) then
		game:switchState(MenuState())
		log.info("Menu")
	end

	if UI.button("Retry", 10, HEIGHT - 30 - 24, 64, 22) then
		game:switchState(InGameState())
		log.info("Retry")
	end
end