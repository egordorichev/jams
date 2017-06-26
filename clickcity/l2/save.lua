local data = {}
local filename = "save.dat"

if love.filesystem.exists(filename) then
	data = lume.deserialize(love.filesystem.read(filename))
end

Save = {}

function Save.flush()
	love.filesystem.write(filename, lume.serialize(data))
end

function Save.clear()
	data = {}
end

setmetatable(Save, {
	__index = function(t, k)
		return data[k]
	end,

	__newindex = function(t, k, v)
		if type(v) == "table" then
			error("Save does not support nested tables")
		end

		data[k] = v
	end
})
