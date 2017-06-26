return {
  version = "1.1",
  luaversion = "5.1",
  tiledversion = "0.14.2",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 25,
  height = 50,
  tilewidth = 8,
  tileheight = 8,
  nextobjectid = 9,
  properties = {},
  tilesets = {
    {
      name = "tiles",
      firstgid = 1,
      tilewidth = 8,
      tileheight = 8,
      spacing = 0,
      margin = 0,
      image = "../images/tiles.png",
      imagewidth = 128,
      imageheight = 128,
      tileoffset = {
        x = 0,
        y = 0
      },
      properties = {},
      terrains = {},
      tilecount = 256,
      tiles = {}
    }
  },
  layers = {
    {
      type = "tilelayer",
      name = "Tile Layer 1",
      x = 0,
      y = 0,
      width = 25,
      height = 50,
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      properties = {},
      encoding = "lua",
      data = {
        7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9,
        23, 51, 49, 50, 50, 51, 50, 49, 49, 51, 51, 50, 49, 49, 49, 50, 51, 51, 51, 50, 51, 51, 49, 49, 25,
        23, 50, 49, 49, 49, 51, 50, 49, 50, 50, 49, 51, 50, 53, 54, 50, 51, 49, 50, 49, 50, 51, 50, 50, 25,
        23, 50, 51, 51, 49, 53, 54, 49, 49, 50, 51, 51, 51, 69, 70, 51, 49, 50, 49, 49, 50, 49, 49, 49, 25,
        23, 50, 49, 49, 50, 69, 70, 49, 49, 49, 51, 51, 51, 50, 49, 50, 49, 49, 50, 50, 49, 50, 50, 50, 25,
        23, 51, 51, 50, 51, 51, 49, 51, 49, 51, 50, 50, 51, 51, 49, 51, 51, 51, 51, 49, 51, 51, 51, 51, 25,
        23, 51, 50, 51, 51, 51, 49, 50, 51, 50, 51, 49, 51, 50, 50, 50, 50, 51, 53, 54, 51, 50, 49, 49, 25,
        23, 49, 50, 51, 50, 13, 49, 49, 51, 49, 50, 49, 52, 49, 51, 49, 49, 50, 69, 70, 49, 50, 51, 51, 25,
        23, 51, 49, 50, 49, 50, 50, 50, 50, 51, 53, 54, 51, 50, 49, 49, 49, 49, 51, 49, 50, 50, 50, 50, 25,
        23, 51, 50, 50, 49, 49, 49, 49, 49, 51, 69, 70, 51, 49, 49, 50, 51, 49, 50, 50, 50, 50, 51, 49, 25,
        23, 51, 49, 50, 50, 50, 49, 50, 51, 50, 51, 51, 49, 51, 50, 50, 49, 49, 49, 49, 49, 51, 49, 51, 25,
        23, 51, 50, 49, 49, 50, 51, 51, 50, 49, 49, 50, 50, 50, 49, 51, 49, 50, 49, 49, 51, 51, 50, 49, 25,
        23, 51, 49, 49, 49, 51, 49, 50, 50, 49, 49, 51, 50, 51, 49, 50, 50, 49, 49, 50, 53, 54, 49, 50, 25,
        23, 49, 49, 51, 49, 50, 50, 51, 49, 49, 50, 51, 50, 53, 54, 50, 50, 50, 50, 50, 69, 70, 51, 50, 25,
        23, 50, 51, 49, 53, 54, 50, 51, 50, 51, 51, 49, 50, 69, 70, 51, 49, 50, 49, 50, 49, 51, 50, 50, 25,
        23, 49, 51, 51, 69, 70, 51, 49, 53, 54, 51, 51, 51, 53, 54, 51, 49, 51, 51, 49, 49, 49, 49, 50, 25,
        23, 51, 49, 50, 50, 49, 53, 54, 69, 70, 49, 51, 49, 69, 70, 51, 50, 51, 51, 50, 50, 13, 50, 51, 25,
        23, 52, 50, 51, 51, 50, 69, 70, 50, 51, 50, 51, 50, 51, 51, 50, 50, 49, 50, 49, 51, 50, 50, 51, 25,
        23, 51, 51, 49, 51, 50, 49, 50, 51, 49, 51, 49, 51, 51, 49, 49, 50, 51, 49, 53, 54, 50, 51, 51, 25,
        23, 49, 51, 50, 51, 49, 50, 49, 49, 51, 49, 51, 51, 49, 50, 49, 49, 51, 50, 69, 70, 53, 54, 50, 25,
        23, 49, 50, 50, 51, 50, 49, 50, 49, 51, 50, 49, 51, 50, 49, 51, 51, 49, 49, 51, 51, 69, 70, 49, 25,
        23, 51, 50, 49, 49, 49, 50, 51, 49, 51, 49, 49, 49, 49, 53, 54, 51, 50, 53, 54, 50, 50, 49, 50, 25,
        23, 49, 50, 49, 50, 50, 51, 51, 49, 50, 51, 50, 49, 51, 69, 70, 50, 50, 69, 70, 51, 50, 50, 49, 25,
        23, 51, 50, 51, 51, 49, 50, 51, 49, 50, 51, 51, 50, 51, 50, 50, 50, 50, 51, 51, 51, 53, 54, 49, 25,
        23, 51, 50, 49, 49, 50, 13, 51, 50, 51, 50, 49, 51, 49, 49, 49, 51, 50, 49, 50, 49, 69, 70, 49, 25,
        23, 51, 49, 50, 49, 50, 50, 49, 50, 51, 51, 51, 49, 50, 50, 51, 51, 53, 54, 50, 50, 50, 53, 54, 25,
        23, 51, 51, 49, 51, 50, 50, 51, 51, 49, 53, 54, 51, 50, 50, 49, 51, 69, 70, 53, 54, 51, 69, 70, 25,
        23, 51, 49, 49, 50, 50, 50, 50, 50, 50, 69, 70, 50, 51, 49, 50, 51, 49, 51, 69, 70, 50, 51, 50, 25,
        23, 50, 50, 51, 49, 50, 51, 49, 50, 51, 51, 49, 51, 51, 50, 50, 51, 50, 49, 50, 50, 53, 54, 51, 25,
        23, 51, 49, 49, 51, 50, 49, 51, 51, 49, 51, 51, 50, 49, 50, 51, 50, 52, 49, 51, 51, 69, 70, 50, 25,
        23, 49, 51, 49, 49, 50, 49, 49, 49, 49, 51, 50, 51, 49, 51, 53, 54, 50, 50, 51, 49, 51, 50, 50, 25,
        23, 51, 49, 50, 53, 54, 49, 50, 50, 49, 49, 51, 50, 50, 51, 69, 70, 49, 49, 51, 51, 50, 49, 51, 25,
        23, 50, 49, 49, 69, 70, 51, 51, 50, 50, 50, 50, 50, 51, 50, 49, 49, 51, 49, 51, 51, 53, 54, 50, 25,
        23, 50, 51, 50, 51, 50, 50, 49, 50, 50, 50, 51, 51, 49, 50, 51, 51, 50, 50, 51, 51, 69, 70, 50, 25,
        23, 50, 50, 49, 51, 50, 49, 51, 51, 51, 49, 51, 49, 49, 51, 51, 51, 51, 51, 51, 53, 54, 53, 54, 25,
        23, 49, 51, 51, 51, 51, 49, 49, 51, 50, 50, 50, 51, 49, 50, 50, 51, 50, 51, 49, 69, 70, 69, 70, 25,
        23, 51, 53, 54, 49, 50, 50, 50, 50, 51, 51, 49, 49, 50, 50, 49, 50, 51, 51, 49, 50, 51, 49, 50, 25,
        23, 51, 69, 70, 50, 49, 50, 51, 49, 50, 51, 51, 50, 49, 49, 49, 51, 49, 50, 49, 49, 50, 51, 51, 25,
        23, 51, 49, 50, 50, 53, 54, 51, 53, 54, 50, 49, 50, 49, 50, 51, 50, 49, 51, 49, 50, 49, 49, 50, 25,
        23, 51, 50, 53, 54, 69, 70, 50, 69, 70, 49, 49, 51, 50, 51, 51, 49, 51, 49, 50, 51, 51, 51, 49, 25,
        23, 13, 50, 69, 70, 51, 50, 50, 51, 49, 51, 50, 51, 51, 50, 51, 50, 50, 51, 50, 51, 51, 51, 50, 25,
        23, 53, 54, 50, 53, 54, 49, 53, 54, 51, 49, 49, 50, 51, 49, 51, 50, 50, 50, 49, 50, 50, 49, 51, 25,
        23, 69, 70, 49, 69, 70, 49, 69, 70, 50, 49, 50, 50, 49, 49, 50, 49, 50, 49, 51, 49, 51, 49, 13, 25,
        23, 50, 50, 50, 51, 50, 53, 54, 51, 50, 49, 49, 49, 50, 50, 51, 49, 50, 49, 51, 49, 51, 50, 49, 25,
        23, 49, 51, 50, 50, 51, 69, 70, 13, 53, 54, 51, 51, 51, 49, 50, 51, 51, 51, 50, 51, 50, 51, 50, 25,
        23, 50, 49, 50, 51, 50, 51, 51, 51, 69, 70, 50, 49, 50, 51, 50, 49, 49, 50, 49, 49, 50, 50, 50, 25,
        23, 50, 50, 49, 50, 51, 50, 50, 50, 49, 51, 49, 51, 49, 50, 51, 50, 50, 49, 51, 49, 49, 50, 51, 25,
        23, 51, 51, 51, 51, 49, 50, 51, 49, 50, 51, 51, 51, 51, 49, 49, 49, 51, 51, 49, 50, 51, 50, 49, 25,
        23, 50, 50, 49, 51, 49, 50, 49, 50, 49, 50, 50, 50, 49, 50, 49, 51, 49, 49, 49, 51, 49, 49, 50, 25,
        39, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 41
      }
    },
    {
      type = "objectgroup",
      name = "T-18",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      properties = {},
      objects = {
        {
          id = 1,
          name = "",
          type = "",
          shape = "rectangle",
          x = 16,
          y = 360,
          width = 16,
          height = 16,
          rotation = 0,
          visible = true,
          properties = {}
        }
      }
    },
    {
      type = "objectgroup",
      name = "player",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      properties = {},
      objects = {
        {
          id = 2,
          name = "",
          type = "",
          shape = "rectangle",
          x = 160,
          y = 16,
          width = 16,
          height = 16,
          rotation = 0,
          visible = true,
          properties = {}
        }
      }
    },
    {
      type = "objectgroup",
      name = "R-10",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      properties = {},
      objects = {
        {
          id = 3,
          name = "",
          type = "",
          shape = "rectangle",
          x = 24,
          y = 200,
          width = 16,
          height = 16,
          rotation = 0,
          visible = true,
          properties = {}
        },
        {
          id = 4,
          name = "",
          type = "",
          shape = "rectangle",
          x = 144,
          y = 288,
          width = 16,
          height = 16,
          rotation = 0,
          visible = true,
          properties = {}
        }
      }
    },
    {
      type = "objectgroup",
      name = "spawner",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      properties = {},
      objects = {
        {
          id = 5,
          name = "",
          type = "",
          shape = "rectangle",
          x = 64,
          y = 88,
          width = 8,
          height = 8,
          rotation = 0,
          visible = true,
          properties = {}
        },
        {
          id = 6,
          name = "",
          type = "",
          shape = "rectangle",
          x = 120,
          y = 192,
          width = 8,
          height = 8,
          rotation = 0,
          visible = true,
          properties = {}
        },
        {
          id = 8,
          name = "",
          type = "",
          shape = "rectangle",
          x = 56,
          y = 280,
          width = 8,
          height = 8,
          rotation = 0,
          visible = true,
          properties = {}
        }
      }
    }
  }
}