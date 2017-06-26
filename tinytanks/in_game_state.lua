InGameState = State:extend()
local live = Assets.load("data/images/live.png")

function InGameState:init()
	InGameState.super.init(self)

	HANDICAP = game.level.handicap

	map.scene = nil
	player = Player(player.name)
	self.name = "ingame"
	self.scene:add(player)

	log.info("Loading data/maps/" .. game.level.map .. ".lua")
	map:loadLua("data/maps/" .. game.level.map .. ".lua", lume.fn(self.loadObject, self))

	map:loadMetaImage("data/images/meta.png", {
		[ "#ffffff" ] = function(t)
			t.solid = true
		end,
		[ "#ffff00" ] = function(t)
			t.solid = true
			t.breakable = true
		end,
		[ "#ff0000" ] = function(t)
			t.solid = true
			-- t.tree = true
		end
	})

	self.scene:add(map)
	self.scene.camera.follow = player
	self.dead = false
	self.deadTimer = 0
	self.won = false
end

function InGameState:loadObject(layer, obj)
	if layer == "T-18" then
		local e = Bot("T-18")

		e.x = obj.x
		e.y = obj.y

		self.scene:add(e)
	elseif layer == "T-12" then
		local e = Bot("T-12")

		e.x = obj.x
		e.y = obj.y

		self.scene:add(e)
	elseif layer == "R-10" then
		local e = Bot("R-10")

		e.x = obj.x
		e.y = obj.y

		self.scene:add(e)
	elseif layer == "B-47" then
		local e = Bot("B-47")

		e.x = obj.x
		e.y = obj.y

		self.scene:add(e)
	elseif layer == "spawner" then
		local e = Spawner()

		e.x = obj.x
		e.y = obj.y

		self.scene:add(e)
	elseif layer == "player" then
		player.x = obj.x
		player.y = obj.y
	end
end

function InGameState:destroy()
	InGameState.super.destroy(self)
end

function InGameState:update(dt)
	InGameState.super.update(self, dt)

	if self.dead then
		self.deadTimer = self.deadTimer - dt
		if self.deadTimer <= 0 then
			self.dead = false
			player:reset()
		end
		return
	elseif player.dead then
		self.dead = true
		player.lives = player.lives - 1
		self.deadTimer = 3

		if player.lives < 0 then
			game:switchState(GameOverState())
		end

		return
	elseif self.won then
		return
	end

	if Input.isDown "moveForward" then
		player:moveForward()
	end

	if Input.isDown "moveBackward" then
		player:moveBackward()
	end

	if Input.isDown "action2" then
		if Input.isDown "rotateLeft" then
			player.cannon:rotateLeft()
		end

		if Input.isDown "rotateRight" then
			player.cannon:rotateRight()
		end
	else
		if Input.isDown "rotateLeft" then
			player:rotateLeft()
		end

		if Input.isDown "rotateRight" then
			player:rotateRight()
		end
	end

	if JOYSTICK ~= nil then
		local x = JOYSTICK:getAxis(2)
		local y = JOYSTICK:getAxis(1)
		local b = JOYSTICK:isDown(1) or JOYSTICK:isDown(2) or JOYSTICK:isDown(3) or JOYSTICK:isDown(4)

		if b then
			if y > 0 then
				player.cannon:rotateRight()
			end

			if y < 0 then
				player.cannon:rotateLeft()
			end
		else
			if y > 0 then
				player:rotateRight()
			end

			if y < 0 then
				player:rotateLeft()
			end
		end

		if x < 0 then
			player:moveForward()
		end

		if x > 0 then
			player:moveBackward()
		end

		if JOYSTICK:isDown(6) or JOYSTICK:isDown(5) then
			player.cannon:shoot()
		end
	end

	if Input.isDown "action1" then
		player.cannon:shoot()
	end

	local found = false

	for k, e in pairs(self.scene.entities) do
		if e:is(Bot) and not e.dead then
			found = true
			break
		end
	end

	if not found then
		self.scene.camera.follow = Bot.last
		self.won = true
		tick.delay(lume.fn(self.toWin, self), 3)
	end
end

function InGameState:toWin()
	game:switchState(WonState())
end

function InGameState:draw()
	self.scene.camera:set()
	InGameState.super.draw(self)
	self.scene.camera:unset()
end

local last = player.health

function InGameState:drawUI()
	if self.dead then
		local s = "Respawning in " .. tostring(lume.round(self.deadTimer, 0.01)) .. ".."
		self.x = self.x or (WIDTH - font:getWidth(s)) / 2
		Util.drawTextWithStroke(s, self.x, (HEIGHT - 5) / 2)
	end

	if last ~= player.health then
		last = last + (player.health - last) * 0.1
	end

	UI.healthBar(4, 4, 64, 11, player.health, player.info.health, last)
	love.graphics.setColor(29, 43, 83)
	love.graphics.rectangle("fill", 4, 15, Util.map(player.info.reloadTime - player.cannon.delay, 0, player.info.reloadTime, 0, 64), 2)
	love.graphics.setColor(255, 255, 255)

	for i = 0, player.lives - 1 do
		love.graphics.draw(live, WIDTH - 15 - i * 14, 4)
	end
end