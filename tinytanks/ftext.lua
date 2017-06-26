FText = Entity:extend()

function FText:new(at, text)
	FText.super.new(self)

	self.solid = false
	self.moves = true
	self.text = text
	self.x = at.x + (at.w - font:getWidth(self.text)) / 2
	self.y = at.y
	self.start = self.y
	self:reset()
	self.tween = flux.group()
	self.zIndex = 10

	self:to(self, .6, { y = self.y - 6 }):ease("elasticout")
end

function FText:reset(text)
	self.killTimer = .6

	if text then
		self.text = text
    	self.y = self.start
	end
end

function Entity:to(...)
	if type(select(1, ...)) == "table" then
		return self.tween:to(...)
	else
 		return self.tween:to(self, ...)
	end
end

function FText:update(dt)
	FText.super.update(self, dt)
	self.tween:update(dt)
	self.killTimer = self.killTimer - dt

	if self.killTimer <= 0 then
		self:kill()
	end
end

function FText:draw()
	Util.drawTextWithStroke(self.text, self.x, self.y)
end