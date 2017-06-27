Entity = Rect:extend()

function Entity:new(options)
    Entity.super.new(self)

    self.last = Rect()
    self.velocity = Point()
    self.accel = Point()
    self.maxVelocity = Point(1000, 1000)
	self.moves = false
	self.bounce = 0
	self.origin = Point()

	self.angle = 0
    self.angularVelocity = 0

    self.static = false
    self.immovable = false
    self.solid = false
    self.touching = {}

    self.dead = false
    self.zIndex = 0
    self.health = 1
    self.scene = nil

    self.frameSize = Rect()
    self.frames = {}
    self.frame = 1
    self.animations = {}
    self.animation = nil
    self.animationTimer = 0
    self.animationFrame = 1

    self.flip = false
    self.autoFlip = false
    self.color = nil
    self.shader = nil
    self.alpha = 1

	self.removable = true

    self.flashColor = { 255, 255, 255 }
    self.flashTimer = 0
    self.flickerTimer = 0

	self.id = "entity"

    if options ~= nil then
            for k, v in pairs(options) do
                    self[k] = v
            end
    end
end

function Entity:onAdd(scene)

end

function Entity:onRemove(scene)

end

function Entity:onClick(btn)

end

function Entity:separate(e, axis)
    if self == e then
        return
    end

    if not self:overlaps(e) then
        return
    end

    local separateX = false

    if axis then
        separateX = (axis == "x")
    else
        if self.last:overlapsY(e.last) then
            if self.last:overlapsX(e.last) then
                local distX = self:centerX() < e:centerX() and
                    e:left() - self:left() or self:right() - e:right()

                local distY = self:centerY() < e:centerY() and
                    e:top() - self:top() or self:bottom() - e:bottom()

                separateX = distX > distY
            else
                separateX = true
            end
        end
    end

    if self.immovable or e.immovable then
        if self.immovable and e.immovable then
            return
        end

        if self.immovable then
            return e:separate(self)
        end

        local sides = e.collidable

        if sides then
            if self.last:overlaps(e.last) then
                return
            end

            if separateX then
                if self.last.x < e.last.x then
                    if not sides.left then return end
                else
                    if not sides.right then return end
                end
            else
                if self.last.y < e.last.y then
                    if not sides.top then return end
                else
                    if not sides.bottom then return end
                end
            end
        end

        if separateX then
            local z = self.last.x < e.last.x

            if z then
                self:right(e:left())

                self.touching.right = true
                e.touching.left = true
            else
                self:left(e:right())

                self.touching.left = true
                e.touching.right = true
            end

            if z == (self.velocity.x > 0) then
                self.velocity.x = -self.velocity.x * self.bounce
            end
        else
            local z = self.last.y < e.last.y

            if z then
                self:bottom(e:top())

                self.touching.bottom = true
                e.touching.top = true
            else
                self:top(e:bottom())

                self.touching.top = true
                e.touching.bottom = true
            end

            if z == (self.velocity.y > 0) then
                self.velocity.y = -self.velocity.y * self.bounce
            end
        end
        return
    end

    if separateX then
        local z = self.last.x < e.last.x
        local mid = self:centerX() < e:centerX() and
            (self:right() + e:left()) / 2 or (self:left() + e:right()) / 2

        if z then
            self:right(mid)
            e:left(mid)

            self.touching.right = true
            e.touching.left = true
        else
            self:left(mid)
            e:right(mid)

            self.touching.left = true
            e.touching.right = true
        end

        if z == (self.velocity.x > 0) then
            self.velocity.x = -self.velocity.x * self.bounce
        end

        if z == (e.velocity.x < 0) then
            e.velocity.x = -e.velocity.x * e.bounce
        end
    else
        local z = self.last.y < e.last.y
        local mid = self:centerY() < e:centerY() and
            (self:bottom() + e:top()) / 2 or (self:top() + e:bottom()) / 2

        if z then
            self:bottom(mid)
            e:top(mid)

            self.touching.bottom = true
            e.touching.top = true
        else
            self:top(mid)
            e:bottom(mid)

            e.touching.bottom = true
            self.touching.top = true
        end

        if z == (self.velocity.y > 0) then
            self.velocity.y = -self.velocity.y * self.bounce
        end

        if z == (e.velocity.y < 0) then
            e.velocity.y = -e.velocity.y * e.bounce
        end
    end
end

function Entity:getNearbyEntities(maxDistance, filter)
    local s = game.state.scene
    maxDistance = maxDistance + math.min(self.w, self.h)
    local x, y = self:centerX(), self:centerY()

    return s:getEntitiesWithinRadius(x, y, maxDistance, self, filter)
end

function Entity:onOverlap(e)
    if self.solid and e.solid then
        self:separate(e)
    end
end

function Entity:hurt(points)
	points = points or 1
    self.health = self.health - points
	self:flicker()

	if self.health <= 0 and not self.dead then
    	self:kill()
    end
end

function Entity:kill()
	self.dead = true
end

function Entity:flicker(n)
	self.flickerTimer = n or 0.5
end

function Entity:flash(n, r, g, b)
	self.flashTimer = n or 0.1
	self.flashColor[1] = r or 255
	self.flashColor[2] = g or 255
	self.flashColor[3] = b or 255
end

function Entity:randomFrame()
	self.frame = math.random(#self.frames)
end

function Entity:warp(x, y)
	assert(type(x) == "number" and type(y) == "number", "expected two numbers")
	self.x = x
	self.y = y

	self.last = Rect.clone(self, self.last)

	if self.scene then
		self.scene.sh:update(self, self.x, self.y)
	end
end

function Entity:followPath(points, speed, loop)
	points = lume.map(points, tonumber)

	assert(#points % 2 == 0, "expected number of points divisible by 2")
	assert(#points > 0, "expected number of points greater than zero")

	self:warp(points[1], points[2])

	if #points == 2 then
		    return
	end

	self.path = {
	    points = points,
	    speed = math.abs(speed),
	    loop = (loop == nil) and true or loop,
	    idx = -1,
	    timer = 0,
	    tween = nil
	}
end


function Entity:clearPath()
	if self.path and self.path.tween then
		self.path.tween:stop()
	end

	self.path = nil
end


function Entity:to(...)
	if type(select(1, ...)) == "table" then
		return self.tween:to(...)
	else
		return self.tween:to(self, ...)
	end
end

function Entity:playAnimation(name, reset)
	local last = self.animation
	self.animation = self.animations[name]

	if reset or self.animation ~= last then
		self.animationTimer = self.animation.period
    	self.animationFrame = 1
    	self.frame = lume.first(self.animation.frames)
	end
end

function Entity:stopAnimation()
	self.animation = nil
end

function Entity:addAnimation(name, frames, fps, loop)
	self.animations[name] = {
	    frames = lume.clone(frames),
    	period = (fps ~= 0) and (1 / math.abs(fps)) or 1,
    	loop = (loop == nil) and true or loop,
	}
end

function Entity:loadImage(filename, width, height)
	if type(filename) == "userdata" then
    	self.image = filename
	else
    	self.image = Assets.load(filename)
	end

	self.image:setFilter("nearest")

	width = width or self.image:getWidth()
	height = height or self.image:getHeight()

	self.frames = {}
	self.frameSize:set(0, 0, width, height)

	for y = 0, self.image:getHeight() / height - 1 do
		for x = 0, self.image:getWidth() / width - 1 do
			local q = love.graphics.newQuad(x * width, y * height, width, height, self.image:getDimensions())
			table.insert(self.frames, q)
		end
	end

	self.w = self.w ~= 0 and self.w or width
	self.h = self.h ~= 0 and self.h or height
end

function Entity:makeImage(width, height, r, g, b, a)
 	self.image = love.graphics.newCanvas(width, height)
	self.image:setFilter("nearest")

	self.image:renderTo(function()
		if r then
	    	love.graphics.clear(r, g, b, a)
	    else
	    	love.graphics.clear(0, 0, 0, 0)
	    end
	end)

	self.width, self.height = width, height
	self.frameSize:set(0, 0, width, height)

	self.frames = {
		love.graphics.newQuad(0, 0, width, height, self.image:getDimensions())
	}
end

function Entity:playSound(filename, gain, always)
	if not game.state.scene.camera:overlaps(self) and not always then
		return
	end

	local sound = Assets.load(filename)

	sound:setVolume(gain or 1)
	sound:rewind()

	if SOUNDS == 1 then
		sound:play()
	end

	return sound
end

function Entity:updateMovement(dt)
	if dt == 0 then
		return
	end

	self.last = Rect.clone(self, self.last)

	self.velocity.x = self.velocity.x + self.accel.x * dt
	self.velocity.y = self.velocity.y + self.accel.y * dt

	if math.abs(self.velocity.x) > self.maxVelocity.x then
		self.velocity.x = self.maxVelocity.x * lume.sign(self.velocity.x)
	end

	if math.abs(self.velocity.y) > self.maxVelocity.y then
		self.velocity.y = self.maxVelocity.y * lume.sign(self.velocity.y)
	end

	self.x = self.x + self.velocity.x * dt
	self.y = self.y + self.velocity.y * dt

	self.angle = self.angle + self.angularVelocity * dt
end

function Entity:updateAutoFlip()
	if self.accel.x ~= 0 then
		self.flip = (self.accel.x < 0)
	end
end

function Entity:updateAnimation(dt)
	local a = self.animation

	if not a then
		return
	end

	self.animationTimer = self.animationTimer - dt

	if self.animationTimer <= 0 then
		self.animationFrame = self.animationFrame + 1

		if self.animationFrame > #a.frames then
			if a.loop == true then
				self.animationFrame = 1
			else
				self:stop()

				if type(a.loop) == "function" then
					a.loop()
				end

				return
			end
		end

		self.animationTimer = self.animationTimer + a.period
		self.frame = a.frames[self.animationFrame]
	end
end

function Entity:updatePathFollow(dt)
	local p = self.path

	if not p then
		return
	end

	if p.timer <= 0 then
		p.idx = p.idx + 2

		if p.idx >= #p.points - 2 then
			if p.loop then
				p.idx = 1
			else
				self.path = nil
				return
			end
		end

		local a = p.points
		local d = lume.distance(a[p.idx], a[p.idx + 1], a[p.idx + 2], a[p.idx + 3])
		local t = d / p.speed

		p.tween = self.tween:to(self, t, { x = a[p.idx + 2], y = a[p.idx + 3] }):ease("linear")
		p.timer = t
	else
		p.timer = p.timer - dt
	end
end

function Entity:updateTouching(dt)
	if dt == 0 then
		return
	end

	lume.clear(self.touching)
end

function Entity:updateTimers(dt)
	self.flashTimer = self.flashTimer - dt
	self.flickerTimer = self.flickerTimer - dt
end

function Entity:update(dt)
	if self.moves then
		self:updateMovement(dt)
	end

	if self.path then
		self:updatePathFollow(dt)
	end

	if self.autoFlip then
		self:updateAutoFlip(dt)
	end

	if self.animation then
		self:updateAnimation(dt)
	end

	self:updateTouching(dt)
	self:updateTimers(dt)
end

function Entity:getDrawArgs()
	return self.frames[self.frame], self.x + self.origin.x, self.y + self.origin.y, math.rad(self.angle), 1, 1,
	self.origin.x, self.origin.y
end

function Entity:getDrawColorArgs()
	local r, g, b = 255, 255, 255

	if self.color then
		r, g, b = unpack(self.color)
	end

	return r, g, b, self.alpha * 255
end

local flashShader = love.graphics.newShader [[
	vec4 effect(vec4 color, Image tex, vec2 tc, vec2 pc) {
		return Texel(tex, tc) + vec4(color.r, color.g, color.b, 0);
	}
]]

function Entity:draw()
	if not self.image then
		return
	end

	if self.flickerTimer > 0 and self.flickerTimer % .06 < .03 then
		return
	end

	local colorSet

	if self.color or self.alpha ~= 1 then
		love.graphics.setColor(self:getDrawColorArgs())
		colorSet = true
	end

	local shader = self.shader

	if self.flashTimer > 0 then
		love.graphics.setColor(unpack(self.flashColor))
		shader = flashShader
		colorSet = true
	end

	local s = love.graphics.getShader()
	love.graphics.setShader(shader)

	love.graphics.draw(self.image, self:getDrawArgs())

	if colorSet then
		love.graphics.setColor(255, 255, 255)
	end

	love.graphics.setShader(s)
end