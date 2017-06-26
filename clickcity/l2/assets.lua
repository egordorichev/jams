Assets = Object:extend()
Assets.map = {}

function Assets.load(filename)
	local res = Assets.map[filename]

	if not res then
	    if filename:match("%.png$") then
	 		res = love.graphics.newImage(filename)
			res:setFilter("nearest")
	    elseif filename:match("%.ogg$") or filename:match("%.wav") then
			res = love.audio.newSource(filename)
	    else
			res = love.filesystem.read(filename)
	    end

		Assets.map[filename] = res
	end

	return res
end

function Assets.clear()
	Assets.map = {}
end