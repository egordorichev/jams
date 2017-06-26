MenuState = State:extend()

function MenuState:init()
	MenuState.super.init(self)

	self.name = "menu"
	self.state = "menu"
end

function MenuState:update(dt)
	MenuState.super.update(self, dt)
end

local variant1 = 1
local variant2 = 1
local s1 = 1
local s2 = 1

local levels = require "data.levels"
local levelNames = { "tutorial", "1", "2", "3" }

local tanks = require "data.tanks"
local tankNames = { "T-18", "T-12", "R-10", "B-47" }

function MenuState:draw()
	MenuState.super.draw(self)

	if self.state == "menu" then
		Util.drawTextWithStroke("Tiny Tanks", 15, 15)

		if UI.button("Play", 10, HEIGHT - 30 - 48 - 24, 64, 22) then
			self.state = "level"
			log.info("Play")
		end

		if UI.button("About", 10, HEIGHT - 30 - 48, 64, 22) then
			log.info("About")
			self.state = "about"
		end

		if UI.button("Settings", 10, HEIGHT - 30 - 24, 64, 22) then
			log.info("Settings")
			self.state = "settings"
		end

		if UI.button("Exit", 10, HEIGHT - 30, 64, 22) then
			log.info("Exit")
			love.event.quit()
		end
	elseif self.state == "about" then
		Util.drawTextWithStroke("Tiny Tanks", 15, 15)
		Util.drawTextWithStroke("by @egordorichev", 15, 25)
		Util.drawTextWithStroke("made for remake jam", 15, 35)

		Util.drawTextWithStroke("Controls:", 15, 55)
		Util.drawTextWithStroke("WASD / arrows - move", 15, 65)
		Util.drawTextWithStroke("x / v / k - shoot", 15, 75)
		Util.drawTextWithStroke("c / l - hold to rotate", 15, 85)
		Util.drawTextWithStroke("        cannon with arrows", 15, 95)

		if UI.button("Back", 10, HEIGHT - 30, 64, 22) then
			log.info("Back")
			self.state = "menu"
		end
	elseif self.state == "settings" then
		s1, MUSIC = UI.select({ "on", "off" }, MUSIC, 10, 10, 64, 22, "Music:")
		s2, SOUNDS = UI.select({ "on", "off" }, SOUNDS, 10, 34, 64, 22, "Sounds:")

		if s1 then
			if MUSIC == 1 then
				music:play()
			else
				music:pause()
			end
		end

		if UI.button("Back", 10, HEIGHT - 30, 64, 22) then
			log.info("Back")
			self.state = "menu"
			variant1 = 1
			variant2 = 1
		end
	elseif self.state == "level" then
		Util.drawTextWithStroke("Select level:", 15, 15)
		s1, variant1 = UI.select(levelNames, variant1, 10, 35, 64, 22)

		local l = nil
		local lk

		for k, l2 in pairs(levels) do
			if k == levelNames[variant1] then
				l = l2
				lk = k
				break
			end
		end

		if lk == "tutorial" or Save[lk] then
			Util.drawTextWithStroke("Handicap: " .. l.handicap, 15, 59)
			Util.drawTextWithStroke("Map: " .. l.map, 15, 69)

			if UI.button("Ok", 10, HEIGHT - 54, 64, 22) then
				log.info("Level selected")
				self.state = "tank"
				game.level = l
				variant1 = 1
				variant2 = 1
			end
		else
			Util.drawTextWithStroke("Locked!", 15, 59)
		end

		if UI.button("Back", 10, HEIGHT - 30, 64, 22) then
			log.info("Back")
			self.state = "menu"
			variant1 = 1
			variant2 = 1
		end
	elseif self.state == "tank" then
		Util.drawTextWithStroke("Select tank:", 15, 15)
		s1, variant1 = UI.select(tankNames, variant1, 10, 35, 64, 22)

		local l = nil
		local lk

		for k, l2 in pairs(tanks) do
			if k == tankNames[variant1] then
				l = l2
				lk = k
				break
			end
		end

		if lk == "T-18" or Save[lk] then
			Util.drawTextWithStroke("Health: " .. l.health, 15, 59)
			Util.drawTextWithStroke("Speed: " .. l.speed, 15, 69)
			Util.drawTextWithStroke("Reload time: " .. l.reloadTime, 15, 79)

			if UI.button("Ok", 10, HEIGHT - 54, 64, 22) then
				log.info("Tank selected")
				player = Player(tankNames[variant1])
				game:switchState(InGameState())
			end
		else
			Util.drawTextWithStroke("Locked!", 15, 59)
		end

		if UI.button("Back", 10, HEIGHT - 30, 64, 22) then
			log.info("Back")
			self.state = "level"
			variant1 = 1
			variant2 = 1
		end
	end
end