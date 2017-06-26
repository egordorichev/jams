Scene = Object:extend()

function Scene:new(cellsize)
    self.sh = shash.new(cellsize)
    self.entities = {}
    self.camera = Camera()
    self.updateDitance = nil
    self.drawDistance = WIDTH * 0.1
end

function Scene:destroy()

end

function Scene:add(e)
    assert(e, "Expected entity")
    assert(not e.scene, "Entity is in another scene")

    table.insert(self.entities, e)
    self.sh:add(e, e:get())

    e.scene = self
    e:onAdd(self)
end

function Scene:remove(e)
    assert(e, "Expected entity")
    assert(e.scene == self, "Entity is in another scene")

    lume.remove(self.entities, e)
	self.sh:remove(e)

    e.scene = nil
    e:onRemove()
end

local function identity(...)
	return ...
end

function Scene:getRandomEntity(filter)
    filter = filter or identity
    assert(#self.entities > 0, "Scene contains no entities")

    local e

    repeat
            e = lume.randomchoice(self.entities)
    until filter(e)

    return e
end

function Scene:getEntityWithId(id)
    return lume.match(self.entities, { id = id })
end

function Scene:getEntityWithId(id)
    return lume.match(self.entities, { id = id })
end

local function handleEntityWithinRadius(e, t, t2, x, y, r, omit, filter)
    if e ~= omit and filter(e) then
        local sz = math.min(e.w, e.h)
        local dist = lume.distance(x, y, e:centerX(), e:centerY())

        if dist <= r + sz then
            table.insert(t, e)
            t2[e] = dist
        end
    end
end

local distLookup
local function compareDistLookup(a, b)
    return distLookup[a] < distLookup[b]
end

function Scene:getEntitiesWithinRadius(x, y, r, omit, filter)
    filter = filter or identity

    local t = {}
    local t2 = {}
    local bx, by, bw, bh = x - r, y - r, r * 2, r * 2

    self.sh:each(bx, by, bw, bh, handleEntityWithinRadius, t, t2, x, y, r, omit, filter)

    distLookup = t2
    table.sort(t, compareDistLookup)

    return t
end

local function pushEntity(e, t)
    table.insert(t, e)
end

local function overlapEntities(a, b)
    if a:overlaps(b) then
        a:onOverlap(b)
        b:onOverlap(a)
    end
end

function Scene:update(dt)
    local entities = self.entities

    if self.updateDistance then
        entities = {}
        local x, y, w, h = self.camera:get(self.updateDistance)
        self.sh:each(x, y, w, h, pushEntity, entities)
    end

    for i, e in lume.ripairs(entities) do
        if e.scene == self then
            e:update(dt)
        end

        if e.scene == self then
            self.sh:update(e, e:get())
        end

        if e.scene == self and e.dead and e.removable then
            self:remove(e)
        end
    end

    for i, e in lume.ripairs(entities) do
        if not e.static and e.scene == self then
            self.sh:each(e, overlapEntities, e)
        end
    end

    self.camera:update(dt)
end

local function compareZIndex(a, b)
    return a.zIndex < b.zIndex
end

function Scene:getOnScreenEntities()
    local t = {}
    local x, y, w, h = self.camera:get(self.drawDistance)

    self.sh:each(x, y, w, h, pushEntity, t)
    table.sort(t, compareZIndex)

    return t
end

function Scene:draw()
    local t = self:getOnScreenEntities()

    for i, e in ipairs(t) do
        e:draw()
    end
end