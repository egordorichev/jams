Debug = {
	active = false
}

local imgui = nil

function Debug.init()
	log.info("Debug is active")
	imgui = require "imgui"

	Debug.hook()
end

function Debug.hook()
	love.textinput = lume.combine(love.textinput, function(t)
		if not Debug.active then
			return
		end

		imgui.TextInput(t)
	end)

	love.keypressed = lume.combine(love.keypressed, function(key)
		if not Debug.active then
			return
		end

		imgui.KeyPressed(key)
	end)

	love.keyreleased = lume.combine(love.keyreleased, function(key)
		if not Debug.active then
			return
		end

		imgui.KeyReleased(key)
	end)

	love.mousemoved = lume.combine(love.mousemoved, function(x, y)
		if not Debug.active then
			return
		end

		imgui.MouseMoved(x, y)
	end)

	love.mousepressed = lume.combine(love.mousepressed, function(x, y, button)
		if not Debug.active then
			return
		end

		imgui.MousePressed(button)
	end)

	love.mousereleased = lume.combine(love.mousereleased, function(x, y, button)
		if not Debug.active then
			return
		end

		imgui.MouseReleased(button)
	end)

	love.wheelmoved = lume.combine(love.wheelmoved, function(x, y)
		if not Debug.active then
			return
		end

		imgui.WheelMoved(y)
	end)

	love.quit = lume.combine(love.quit, function(x, y)
		if not Debug.active then
			return
		end

		imgui.ShutDown(y)
	end)
end

local status
local filter = ""

function Debug.draw()
	if not Debug.active then
		return
	end

	imgui.NewFrame()

	if imgui.Begin("Debug") then
		imgui.Text(love.timer.getFPS() .. " FPS")
		imgui.Text("Draw calls: " .. game.drawCalls)

		if  game.state.scene then
			imgui.Text("Entities: " .. game.state.scene.sh:info("entities"))
		end

		imgui.Text("State: " .. game.state.name)
		imgui.End()
	end

	if  game.state.scene then
		if imgui.Begin("Entities") then
			status, filter = imgui.InputText("Filter", filter, 100)
			imgui.BeginChild("Entities")

			for i, e in ipairs(game.state.scene.entities) do
				local name = e.id

				if name:find(filter, 0, true) and imgui.TreeNode(name) then
					Debug.drawEntity(e)
					imgui.TreePop()
				end
			end

			imgui.EndChild()
			imgui.End()
		end
	end
	
	imgui.Render()
end

function Debug.drawEntity(e)
	if e.image then
		imgui.Image(e.image, e.image:getDimensions())
	end

	if imgui.Button("kill") then
		e:kill()
	end

	imgui.SameLine()

	if imgui.Button("hurt") then
		e:hurt()
	end

	imgui.SameLine()

	if imgui.Button("heal") then
		e.health = 1000
	end

	if imgui.Button("flicker") then
		e:flicker()
	end

	imgui.SameLine()

	if imgui.Button("flash") then
		e:flash()
	end

	status, e.health = imgui.DragFloat("health", e.health)
	status, e.x, e.y = imgui.DragFloat2("position", e.x, e.y)
end