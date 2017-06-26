State = Object:extend()

function State:new()
	self.scene = nil
	self.name = "state"
end

function State:init()
	self.scene = Scene(32)
end

function State:destroy()
	self.scene:destroy()
end

function State:update(dt)
	self.scene:update(dt)
end

function State:draw()
	self.scene:draw()
end

function State:drawUI()

end