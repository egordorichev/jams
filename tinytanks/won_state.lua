WonState = State:extend()

local levels = require "data.levels"
local levelNames = { "tutorial", "1", "2", "3" }
local tanks = require "data.tanks"
local tankNames = { "T-18", "T-12", "R-10", "B-47" }

function WonState:init()
	WonState.super.init(self)
	self.name = "won"

	self.hasNext = false
	self.next = nil

	log.info("Current level: " .. game.level.map)

	local i = 1
	local n = false

	for _, l in pairs(levelNames) do
		if n then
			self.next = levels[l]
			self.hasNext = true
			Save[l] = true
			break
		end

		if l == game.level.name then
			n = true

			if l == "tutorial" then
				Save["T-12"] = true
			elseif l == "1" then
				Save["R-10"] = true
			elseif l == "3" then
				Save["B-47"] = true
			end
		end

		i = i + 1
	end
end

function WonState:update(dt)
	WonState.super.update(self, dt)
end

function WonState:draw()
	WonState.super.draw(self)

	Util.drawTextWithStroke("You won!", 15, 15)

	if UI.button("To menu", 10, HEIGHT - 30, 64, 22) then
		game:switchState(MenuState())
		log.info("Menu")
	end

	if self.hasNext then
		if UI.button("Next level", 10, HEIGHT - 30 - 24, 64, 22) then
			game.level = self.next
			game:switchState(InGameState())
			log.info("Next level")
		end
	end
end