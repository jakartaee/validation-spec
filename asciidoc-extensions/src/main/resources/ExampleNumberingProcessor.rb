# encoding: UTF-8
require 'asciidoctor'
require 'asciidoctor/extensions'

class ExampleIndexes
  attr_accessor :sectionIndex
  @sectionIndex
  attr_accessor :exampleIndex
  @exampleIndex
  attr_accessor :tableIndex
  @tableIndex
  def initialize(sect)
    @sectionIndex = sect
    @tableIndex = 1
    @exampleIndex = 1
  end
end

class ExampleNumberingProcessor < Asciidoctor::Extensions::Treeprocessor
  def process document
    document.blocks.each do |block|
      if (block.node_name == 'section')
        processSection(block, ExampleIndexes.new(block.number))
      end
    end
    return document
  end

  def processSection(mainBlock, exampleIndx)
    mainBlock.blocks.each do |block|
      if (block.node_name == 'example')
        block.caption = 'Example '.concat(exampleIndx.sectionIndex.to_s).concat('.').concat(exampleIndx.exampleIndex.to_s).concat(': ')
        exampleIndx.exampleIndex += 1
      elsif (block.node_name == 'table')
        block.caption = 'Table '.concat(exampleIndx.sectionIndex.to_s).concat('.').concat(exampleIndx.tableIndex.to_s).concat(': ')
        exampleIndx.tableIndex += 1
      elsif (block.node_name == 'section')
        processSection(block, exampleIndx)
      end
    end
  end
end

